(ns clj-utils.macros
  (:require [clojure.tools.logging :as logging]))

(defmacro defn-with-logging
  [fn-name log-level args-list & body]
  (let [valid-log-levels [:trace :debug :info :warn :error :fatal]]
    (if-not (some #{log-level} valid-log-levels)
      (throw (Exception. (format "%s is not a valid log level. Valid log levels are: %s" log-level valid-log-levels)))
      (let [log-str (str (format "%s/%s" *ns* fn-name) " %s")]
        `(defn ~fn-name ~args-list
           (do (clojure.tools.logging/logf ~log-level (format ~log-str ~args-list))
               ~@body))))))

(defn- args-list->args-lists-for-defcurried
  [args-list]
  (for [n (->> args-list count inc range)]
    (vec (take n args-list))))

(defn- gen-arity
  [fn-name args-list]
  `(~args-list
    (partial ~fn-name ~@args-list)))

(defmacro defcurried
  [fn-name args-list & body]
  (let [args-lists         (args-list->args-lists-for-defcurried args-list)
        partial-args-lists (drop-last args-lists)
        full-args-list     (last args-lists)]
    `(defn ~fn-name
       ~@(map (fn [partial-args-list]
                (gen-arity fn-name partial-args-list))
              partial-args-lists)
       (~full-args-list ~@body))))

(defmacro defn-with-return-map
  [fn-name ret-map arg-vec & body]
  `(defn ~fn-name
     ~arg-vec
     (let [ret# ~@body]
       (merge ~ret-map
              {:ret ret#}))))

