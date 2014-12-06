(ns clj-utils.core)

(defmacro deftransform
  [transform-name & body]
  `(do ~@(concat
          (->> body
               (partition 2)
               (map (fn [[fn-name# fn-body#]]
                      `(defn ~fn-name#
                         [~'$]
                         ~fn-body#))))
          (list `(defn ~transform-name
                   [~'$]
                   ~(concat (list '-> '$)
                            (->> body
                                 (partition 2)
                                 (map first))))))))

(defmacro defcurried
  [fn-name args-list & body]
  (let [args-list->args-lists-for-defcurried (fn [args-list]
                                               (for [n (->> args-list count inc range)]
                                                 (vec (take n args-list))))
        gen-arity                            (fn [fn-name args-list]
                                               `(~args-list
                                                 (partial ~fn-name ~@args-list)))
        args-lists                           (args-list->args-lists-for-defcurried args-list)
        partial-args-lists                   (drop-last args-lists)
        full-args-list                       (last args-lists)]
    `(defn ~fn-name
       ~@(map (fn [partial-args-list]
                (gen-arity fn-name partial-args-list))
              partial-args-lists)
       (~full-args-list ~@body))))
