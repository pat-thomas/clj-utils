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


