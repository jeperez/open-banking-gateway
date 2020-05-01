# Purpose

This test validates only XS2A protocol by using stubbed Wiremock responses 
(and accommodated to XS2A protocol, so list transactions method doesn't call list accounts) 
of Dynamic Sandbox.

Example of how to record fixtures with wiremock:

```shell script
java -jar 'wiremock-standalone-2.26.3.jar' --port 30000 --proxy-all http://127.0.0.1:20014 \
    --record-mappings --match-headers accept,psu-id,x-request-id,content-type,psu-ip-address --verbose --root-dir ./results
```
This command will record all requests stubs based on what was sent/received to http://127.0.0.1:20014 with
headers: 'accept,psu-id,x-request-id,content-type,psu-ip-address' and store the stubs(fixtures) at `./results` directory.
Also '--verbose' will turn on request/response logging, so you will be able to see if you forgot to include some
headers by checking log output (unfortunately recording all headers directly to stub is an open issue in WireMock repo
https://github.com/tomakehurst/wiremock/pull/1022)