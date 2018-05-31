<#macro title name>
    <title>${name}</title>
</#macro>

<#macro nav menu submenu>
    <nav class="navbar navbar-inverse">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-menu" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">首页</a>
        </div>
        <div id="navbar-menu" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="dropdown <#if menu == "watch">active</#if>">
                    <a href="javascript:" data-toggle="dropdown">
                        转发策略
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li <#if menu == "loadbalance" && submenu == "random">class="active"</#if>><a href="/loadbalance/random/random.html">随机</a></li>
                        <li <#if menu == "loadbalance" && submenu == "weight">class="active"</#if>><a href="/loadbalance/weight/weight.html">权重</a></li>
                        <li <#if menu == "loadbalance" && submenu == "hash">class="active"</#if>><a href="/loadbalance/hash/hash.html">HASH</a></li>
                    </ul>
                </li>
                <li <#if menu == "protocal" && submenu == "protocal">class="active"</#if>><a href="/protocal/protocal.html">服务协议</a></li>
                <li <#if menu == "group" && submenu == "group">class="active"</#if>><a href="/group/group.html">分组</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/logout.do">退出</a></li>
            </ul>
        </div>
    </nav>
</#macro>