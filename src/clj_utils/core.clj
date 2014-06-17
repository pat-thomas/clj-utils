(ns clj-utils.core
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

(defn deep-map
  [function coll]
  (map #(map function %) coll))

(declare swap-with-prev)

(defn swap-with-next
  "Given a collection and an index n, returns that collection
   with the elements at position n and position (n+1) swapped."
  [coll n]
  (if (>= n (-> coll count dec))
    (swap-with-prev coll n)
    (let [[first-part second-part] (split-at (inc n) coll)
          truncated-first-part     (drop-last first-part)
          truncated-second-part    (drop 1 second-part)
          swapped-part             [(first second-part) (last first-part)]]
      (concat truncated-first-part swapped-part truncated-second-part))))

(defn swap-with-prev
  "Given a collection and an index n, returns that collection
   with the elements at position n and position (n-1) swapped."
  [coll n]
  (if (<= n 0)
    (swap-with-next coll n)
    (let [n                        (if (>= n (count coll)) (-> coll count dec) n)
          [first-part second-part] (split-at n coll)
          truncated-first-part     (drop-last first-part)
          truncated-second-part    (drop 1 second-part)
          swapped-part             [(first second-part) (last first-part)]]
      (concat truncated-first-part swapped-part truncated-second-part))))

(defmacro force-repeat
  "Like (repeat), but forces reevaluation of the function call. Useful for non-referentially-transparent functions."
  [len fn-body]
  `(for [_# (range ~len)]
     ~fn-body))

(defn update-nth
  [l n update-fn]
  (let [new-val [(update-fn (nth l n))]
        head    (take (dec n) l)
        tail    (drop n l)]
    (concat head new-val tail)))

(defn collect-truthy-values
  "Like cond, but will return the accumulation of every expression that returns true."
  [& pred-expr-pairs]
  (reduce (fn [acc pair]
            (let [[pred expr] pair]
              (if pred
                (conj acc expr)
                acc)))
          []
          (partition 2 pred-expr-pairs)))

(defn map-values
  "Applies f to every value in the map m."
  [f m]
  (into {}
        (map (fn [pair]
               [(key pair) (f (val pair))])
             m)))

(defmacro make-map
  "Given a list of symbols, creates a map of each symbol converted
   to a keyword, each of which is keyed to the symbol itself."
  [things & opts]
  `(reduce (fn [acc# t#]
             (assoc acc# (-> t# name keyword) t#))
           {}
           '~things))

(defmacro make-fn-alias
  "namespace and fn-name are both keywords."
  [namespace fn-name]
  (assert (and (keyword? namespace)
               (keyword? fn-name)))
  (if-let [looked-up-fn (ns-resolve (symbol (name namespace))
                                      (symbol (name fn-name)))]
    (let [looked-up-fn-argslist (-> looked-up-fn meta :arglists)]
      `(defn ~(symbol (name fn-name))
         ~(first looked-up-fn-argslist)
         (~looked-up-fn ~@(first looked-up-fn-argslist))))
    (throw (Exception. (format "Couldn't locate function %s in namespace %s" (name fn-name) (name namespace))))))
