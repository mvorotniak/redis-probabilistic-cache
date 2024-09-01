# d - delay, c - concurrency, v - verbose (print messages), t - time for testing
siege -d1 -c100 -v -t2m http://localhost:8080/article?id=1