(ns clj-utils.random
  (:require [clj-utils.core :as core-utils]))

(defn randomly-call
  "Given a list of functions, will randomly call one of them with args as the arguments."
  [fn-list & args]
  (let [handler (rand-nth fn-list)]
    (apply handler args)))

(defn randomly-swap
  [coll]
  (randomly-call [core-utils/swap-with-next core-utils/swap-with-prev] coll (rand-int (count coll))))

(defn rand-seq-from-pool
  ([pool len]
     (vec
      (for [_ (range len)]
        (rand-nth pool))))
  ([pool len key]
     (vec
      (for [_ (range len)]
        {key (rand-nth pool)}))))
