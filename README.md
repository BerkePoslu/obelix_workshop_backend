# Obelix & Co. Webshop 🪨

Currently, everything is under `obelix-webshop`, but we have great plans to split parts to split the most important part, my standing stones, into its own microservice.
Gradle subprojects are already in place, ready for the great change.


 grafana setup:
docker compose up -d
browser auf localhost:3000
login admin admin
data sources -> add data source -> prometheus
url: http://prometheus:9090
save und test
dashboards -> new dashboard -> add visualization
select prometheus
metric: menhirs_count
run queries
save dashboard
