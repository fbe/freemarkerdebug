<#import "layout.ftl" as layout>
<@layout.layout>
  <#list 1..10 as x>
  <p>Result of slow sample service: ${slowSampleService.slowComputationResult}</p>
  </#list>
</@layout.layout>


