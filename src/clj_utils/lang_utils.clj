(ns clj-utils.lang-utils)

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

(defn remove-nil-values
  "Removes values from a map that are nil."
  [m]
  (reduce (fn [acc k]
            (if-let [v (get m k)]
              (assoc acc k v)
              acc))
          {}
          (keys m)))

(defn good-merge
  "Merges ms, but does not overwrite keys in m with nil values."
  [& ms]
  (reduce (fn [acc m]
            (merge acc (remove-nil-values m)))
          {}
          ms))
