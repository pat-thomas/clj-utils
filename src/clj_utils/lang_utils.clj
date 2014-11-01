(ns clj-utils.lang-utils)

(defmacro defmapper
  [name impl]
  `(defn ~name
     [anon#]
     (map ~impl anon#)))

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
