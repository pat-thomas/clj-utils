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
