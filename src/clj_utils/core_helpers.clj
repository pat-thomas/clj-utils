(ns clj-utils.core-helpers
  (:require [clojure.walk :as walk]))

(defn autogen-dollar-arg?
  [^clojure.lang.Symbol thing]
  (let [thing-str (str thing)]
    (or (= thing '$)
        (and (symbol? thing)
             (.startsWith thing-str "$")
             (->> thing-str
                  (drop 1)
                  (apply str)
                  (re-matches #"\d+"))))))

(defn autogen-arg->salience
  [el]
  (if (= el '$)
    0
    (->> el str (drop 1) (apply str) Integer/parseInt)))

(defn sort-numbered-autogen-args
  "$ $1 $2 $11 etc."
  [autogen-args]
  (->> autogen-args
       (map (fn [arg]
              {:salience (autogen-arg->salience arg)
               :val      arg}))
       (sort-by :salience)
       (map :val)))

(defn make-numbered-autogen-args-range
  "$ $3 -> $ $1 $2 $3"
  [autogen-args]
  (let [highest (->> autogen-args
                     sort-numbered-autogen-args
                     last
                     str
                     (drop 1)
                     (apply str)
                     Integer/parseInt)]
    (->> (for [i (map inc (range highest))]
           (->> i (str "$") symbol))
         (concat ['$])
         vec)))

(defn identify-numbered-autogen-args
  [form]
  (let [accum (atom [])]
    (walk/postwalk (fn [el]
                     (when (autogen-dollar-arg? el)
                       (swap! accum conj el)))
                   form)
    (-> accum deref distinct sort-numbered-autogen-args make-numbered-autogen-args-range)))
