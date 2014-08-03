(ns clj-utils.maps)

(defn updates-in
  [m & key-update-pairs]
  (if-not (even? (count key-update-pairs))
    (throw (Exception. "number of key-update-pairs must be even."))
    (reduce (fn [acc [lookup-key update-fn]]
              (if-let [nascent-val (get-in m lookup-key)]
                (assoc-in acc lookup-key (update-fn nascent-val))
                m))
            m
            (partition 2 key-update-pairs))))

(defmacro make-map
  "Given a list of symbols, creates a map of each symbol converted
   to a keyword, each of which is keyed to the symbol itself."
  [& ks]
  (apply hash-map
         (mapcat (fn [k]
                   (vector (keyword k) k))
                 ks)))

