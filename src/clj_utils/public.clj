(ns clj-utils.public)

(defn function?
  [^clojure.lang.Symbol target-ns ^clojure.lang.Symbol fn-name]
  (let [resolve-fn-sym (symbol (str target-ns "/" fn-name))]
    (when-let [target (resolve resolve-fn-sym)]
      (and (fn? (var-get target))
           resolve-fn-sym))))

(defn macro?
  [^clojure.lang.Symbol target-ns ^clojure.lang.Symbol macro-name]
  (let [resolve-fn-sym (symbol (str target-ns "/" macro-name))]
    (when-let [target (resolve resolve-fn-sym)]
      (and (:macro (meta target))
           resolve-fn-sym))))

(defn find-fns-and-macros-in-namespace
  [target-ns]
  (let [targets (keys (ns-publics target-ns))
        fns     (map #(function? target-ns %) targets)
        macros  (map #(macro? target-ns %) targets)]
    (->> fns
         (concat macros)
         distinct
         (filter #(not (nil? %))))))

(defn produce-fn-alias-list
  [fn-macro-list]
  (map (fn [target]
         [(symbol (name target)) target])
       fn-macro-list))

(defn require-all-in-ns
  [^clojure.lang.Keyword target-ns]
  (let [ns-sym               (symbol (name target-ns))
        fns-and-macros-in-ns (find-fns-and-macros-in-namespace ns-sym)
        alias-list           (produce-fn-alias-list fns-and-macros-in-ns)]
    `(do (require ~(symbol (str "'" ns-sym)))
         ~@(map (fn [[target# target-with-ns#]]
                  `(def ~target# ~target-with-ns#))
                alias-list))))

(defmacro alias-namespaces
  [& namespaces]
  `(do ~@(map (fn [ns-name]
                (require-all-in-ns ns-name))
              namespaces)))


