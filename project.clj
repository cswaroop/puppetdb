(require '[clojure.string :as s])
(use '[clojure.java.shell :only (sh)]
     '[clojure.java.io :only (file)])

(def version-string
  (memoize
   (fn []
     "Determine the version number using 'rake version -s'"
     []
     (if (.exists (file "version"))
       (s/trim (slurp "version"))
       (let [command                ["rake" "package:version" "-s"]
             {:keys [exit out err]} (apply sh command)]
         (if (zero? exit)
           (s/trim out)
           "0.0-dev-build"))))))

(defproject puppetdb (version-string)
  :description "Puppet-integrated catalog and fact storage"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cheshire "5.1.1"]
                 [org.clojure/core.incubator "0.1.0"]
                 [org.clojure/core.match "0.2.0-alpha9"]
                 [org.clojure/core.memoize "0.5.1"]
                 [org.clojure/math.combinatorics "0.0.2"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.clojure/tools.cli "0.2.1"]
                 [org.clojure/tools.nrepl "0.2.2"]
                 [org.clojure/tools.namespace "0.1.3"]
                 [swank-clojure "1.4.3"]
                 [vimclojure/server "2.3.6" :exclusions [org.clojure/clojure]]
                 [clj-stacktrace "0.2.4"]
                 [metrics-clojure "0.7.0" :exclusions [org.clojure/clojure org.slf4j/slf4j-api]]
                 [clj-time "0.3.7"]
                 [org.clojure/java.jmx "0.1"]
                 ;; Filesystem utilities
                 [fs "1.1.2"]
                 ;; Configuration file parsing
                 [org.ini4j/ini4j "0.5.2"]
                 ;; Version information
                 [trptcolin/versioneer "0.1.0"]
                 ;; Job scheduling
                 [overtone/at-at "1.1.1"]
                 ;; Nicer exception handling with try+/throw+
                 [slingshot "0.10.3"]
                 [digest "1.3.0"]
                 [log4j "1.2.16" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]
                 ;; Database connectivity
                 [com.jolbox/bonecp "0.7.1.RELEASE" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/slf4j-log4j12 "1.6.4"]
                 [org.clojure/java.jdbc "0.1.1"]
                 [org.hsqldb/hsqldb "2.2.8"]
                 [postgresql/postgresql "9.0-801.jdbc4"]
                 [clojureql "1.0.3"]
                 ;; MQ connectivity
                 [clamq/clamq-activemq "0.4" :exclusions [org.slf4j/slf4j-api]]
                 [org.apache.activemq/activemq-core "5.6.0" :exclusions [org.slf4j/slf4j-api org.fusesource.fuse-extra/fusemq-leveldb]]
                 ;; WebAPI support libraries.
                 [net.cgrand/moustache "1.1.0" :exclusions [ring/ring-core org.clojure/clojure]]
                 [clj-http "0.5.3"]
                 [ring/ring-core "1.1.1"]
                 [ring/ring-jetty-adapter "1.1.1"]
                 [org.apache.commons/commons-compress "1.4.1"]]

  :profiles {:dev {:resource-paths ["test-resources"],
                   :dependencies [[ring-mock "0.1.1"]]}}

  :jar-exclusions [#"leiningen/"]

  :aot [com.puppetlabs.puppetdb.core]
  :main com.puppetlabs.puppetdb.core
)
