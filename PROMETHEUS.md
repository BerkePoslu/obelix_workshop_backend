# Prometheus & Grafana Setup

## Micrometer Gauge: obelix.quarry.menhirs.available

die quarry service exponiert eine custom metric für die anzahl verfügbarer hinkelsteine.

### metric details
- **name**: `obelix.quarry.menhirs.available`
- **type**: gauge
- **description**: number of available menhirs in quarry
- **endpoint**: `http://localhost:8081/actuator/prometheus`

### prometheus config
füge zu `prometheus.yml` hinzu:
```yaml
scrape_configs:
  - job_name: 'obelix-quarry'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8081']
```

### check targets
- prometheus ui: `http://localhost:9090/targets`
- status sollte `UP` sein

### query in prometheus
```promql
obelix_quarry_menhirs_available
```

### grafana dashboard
1. add prometheus datasource: `http://localhost:9090`
2. create panel mit query: `obelix_quarry_menhirs_available`
3. visualization: gauge oder graph
