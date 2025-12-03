# prometheus metrics

## gauge metric

metrik name: obelix_quarry_menhirs_available

zeigt anzahl verfügbarer hinkelsteine im quarry service.

## prometheus setup

1. prometheus starten:

docker run -d -p 9090:9090 -v /pfad/file.yml:/etc/prometheus/prometheus.yml prom/prometheus


2. prometheus.yml konfiguration:

scrape_configs:
  - job_name: 'quarry'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8081']


3. prometheus ui öffnen: http://localhost:9090

4. query in prometheus:

obelix_quarry_menhirs_available


## grafana setup

1. grafana starten:

docker run -d -p 3000:3000 grafana/grafana


2. grafana öffnen: http://localhost:3000 (login: admin/admin)

3. data source hinzufügen:
   - configuration -> data sources -> add prometheus
   - url: http://localhost:9090
   - save & test

4. dashboard erstellen:
   - create -> dashboard -> add panel
   - query: obelix_quarry_menhirs_available
   - panel title: "verfügbare hinkelsteine"

## metric testen

endpoint direkt aufrufen:

curl http://localhost:8081/actuator/prometheus | grep obelix_quarry

oder Postman

