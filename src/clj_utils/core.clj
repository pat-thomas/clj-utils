(ns clj-utils.core)

(defn randomly-call
  "Given a list of functions, will randomly call one of them with args as the arguments."
  [fn-list & args]
  (let [handler (rand-nth fn-list)]
    (apply handler args)))

(defn deep-map
  [function coll]
  (map (fn [c] (map function c))
       coll))

(defn rand-seq-from-pool
  ([pool len]
     (vec
      (for [_ (range len)]
        (rand-nth pool))))
  ([pool len key]
     (vec
      (for [_ (range len)]
        {key (rand-nth pool)}))))

(declare swap-with-prev)

(defn swap-with-next
  [coll n]
  (if (>= n (-> coll count dec))
    (swap-with-prev coll n)
    (let [[first-part second-part] (split-at (inc n) coll)
          truncated-first-part     (drop-last first-part)
          truncated-second-part    (drop 1 second-part)
          swapped-part             [(first second-part) (last first-part)]]
      (concat truncated-first-part swapped-part truncated-second-part))))

(defn swap-with-prev
  [coll n]
  (if (<= n 0)
    (swap-with-next coll n)
    (let [n                        (if (>= n (count coll)) (-> coll count dec) n)
          [first-part second-part] (split-at n coll)
          truncated-first-part     (drop-last first-part)
          truncated-second-part    (drop 1 second-part)
          swapped-part             [(first second-part) (last first-part)]]
      (concat truncated-first-part swapped-part truncated-second-part))))

(defn randomly-swap
  [coll]
  (randomly-call [swap-with-next swap-with-prev] coll (rand-int (count coll))))

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
