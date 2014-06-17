(ns clj-utils.seq-utils)

(defn all-equal?
  "Returns true if all elements in s are equal, false otherwise."
  [s]
  (-> s distinct count (= 1)))

(defn singular?
  "Returns true if s is of length 1, false otherwise"
  [s]
  (= (count s) 1))
