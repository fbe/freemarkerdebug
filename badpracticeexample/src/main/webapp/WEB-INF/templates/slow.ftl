<#import "layout.ftl" as layout>
<@layout.layout>
  <#list 1..10 as x>
  <p>Result of slow sample service: ${slowSampleService.slowComputationResult}</p>
  </#list>
  
  <h2>Random display block:</h2>
  
  <#if slowSampleService.slowRandomBoolean()>
  	<div>Random block here</div>
  <#else>
  	<div>Other random block here</div>
  </#if>
  
  <#include "footer.ftl" />
  
</@layout.layout>


