# freemarkerdebug

Implemented freemarker / spring mvc anti patterns. Code used for traceing tool development.

Sample Output:

```
[1442967154684] START for freemarker.core.MixedContent(462794862) -<#import "layout.ftl" as layout>
<@layout.layout>
  <#list 1..10 as x>
  <p>Result of slow sample service: ${slowSampleService.slowComputationResult}</p>
  </#list>
</@layout.layout>



[1442967154684] START for freemarker.core.LibraryLoad(1896283319) -<#import "layout.ftl" as layout>
[1442967154685] START for freemarker.core.Macro(196219800) -<#macro layout>
  <html>
        <head>
                <title>Freemarker Test</title>
        </head>
    <body>
        <h1>Freemaker Debug</h1>
                <#nested/>
        </body>    
  </html>
</#macro>
[1442967154685] END for freemarker.core.Macro(196219800)
[1442967154685] END for freemarker.core.LibraryLoad(1896283319)
[1442967154685] START for freemarker.core.UnifiedCall(878232267) -<@layout.layout>
  <#list 1..10 as x>
  <p>Result of slow sample service: ${slowSampleService.slowComputationResult}</p>
  </#list>
</@layout.layout>
[1442967154685] START for freemarker.core.Macro(196219800) -<#macro layout>
  <html>
        <head>
                <title>Freemarker Test</title>
        </head>
    <body>
        <h1>Freemaker Debug</h1>
                <#nested/>
        </body>    
  </html>
</#macro>
[1442967154686] START for freemarker.core.MixedContent(1939109193) -
  <html>
        <head>
                <title>Freemarker Test</title>
        </head>
    <body>
        <h1>Freemaker Debug</h1>
                <#nested/>
        </body>    
  </html>

[1442967154686] START for freemarker.core.BodyInstruction(1803567910) -<#nested/>
[1442967154687] START for freemarker.core.IteratorBlock(1804340103) -<#list 1..10 as x>
  <p>Result of slow sample service: ${slowSampleService.slowComputationResult}</p>
  </#list>
[1442967154687] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967154791] END for freemarker.core.DollarVariable(462019583)
[1442967154791] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967154891] END for freemarker.core.DollarVariable(462019583)
[1442967154891] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967154991] END for freemarker.core.DollarVariable(462019583)
[1442967154991] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967155092] END for freemarker.core.DollarVariable(462019583)
[1442967155092] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967155192] END for freemarker.core.DollarVariable(462019583)
[1442967155192] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967155292] END for freemarker.core.DollarVariable(462019583)
[1442967155293] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967155393] END for freemarker.core.DollarVariable(462019583)
[1442967155393] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967155493] END for freemarker.core.DollarVariable(462019583)
[1442967155493] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967155593] END for freemarker.core.DollarVariable(462019583)
[1442967155593] START for freemarker.core.DollarVariable(462019583) -${slowSampleService.slowComputationResult}
[1442967155694] END for freemarker.core.DollarVariable(462019583)
[1442967155694] END for freemarker.core.IteratorBlock(1804340103)
[1442967155694] END for freemarker.core.BodyInstruction(1803567910)
[1442967155694] END for freemarker.core.MixedContent(1939109193)
[1442967155694] END for freemarker.core.Macro(196219800)
[1442967155694] END for freemarker.core.UnifiedCall(878232267)
[1442967155694] END for freemarker.core.MixedContent(462794862)
```
