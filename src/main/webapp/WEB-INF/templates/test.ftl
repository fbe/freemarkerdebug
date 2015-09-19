<html>
<head>
  <title>Behinderter Freemarker</title>
</head>
<body>
  <p>My test var is ${test}</p>
  <#list 1..10 as x>
  <p>Result of slow sample service: ${slowSampleService.slowComputationResult}</p>
  </#list>
</body>
</html>