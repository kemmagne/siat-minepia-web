scp -i /home/tadzotsa/.ssh/glassfish_htserver_id_rsa target/siat-ct-web.war glassfish@agserver:~/ \
&& ssh -i /home/tadzotsa/.ssh/glassfish_htserver_id_rsa glassfish@agserver "cp -v siat-ct-web.war /opt/tomcat/webapps/ ; cp -v siat-ct-web.war /data/glassfish/4/domains/siat/autodeploy/ ; rm -vf siat-ct-web.war"
