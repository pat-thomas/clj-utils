(ns clj-utils.seq-utils)

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

(defn update-nth
  [l n update-fn]
  (let [new-val [(update-fn (nth l n))]
        head    (take (dec n) l)
        tail    (drop n l)]
    (concat head new-val tail)))

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

(defn all-equal?
  "Returns true if all elements in s are equal, false otherwise."
  [s]
  (-> s distinct count (= 1)))

(defn singular?
  "Returns true if s is of length 1, false otherwise"
  [s]
  (= (count s) 1))
