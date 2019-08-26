<%-- Copyright 2016-2017 Ellucian Company L.P. and its affiliates. --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="viewport" content="width=device-width, height=device-height,  initial-scale=1.0, user-scalable=no, user-scalable=0"/>
    <g:set var="mep" value="${params?.mepCode}"/>
    <g:set var="hideSSBHeaderComps" value="${params?.hideSSBHeaderComps}" />
    <g:set var="applicationName" value= "${grails.util.Metadata.current.getApplicationName()}"/>
    <g:set var="appName" value= "${System.properties['BANNERXE_APP_NAME']}"/>

    <g:if test="${appName.equals('DirectDeposit')}">
        <g:set var="url" value="${'ssb/directDeposit'}"/>
    </g:if>
    <g:elseif test="${appName.equals('PersonalInformation')}">
        <g:set var="url" value="${'ssb/personalInformation'}"/>
    </g:elseif>
    <g:else>
        <g:set var="url" value="${'ssb/general'}"/>
    </g:else>
    <g:if test="${mep && hideSSBHeaderComps}">
        <g:set var="url" value="${url+mep+'&hideSSBHeaderComps='+hideSSBHeaderComps}" />
    </g:if>
    <g:elseif test="${mep}">
        <g:set var="url" value="${url+'?mepCode='+mep}" />
    </g:elseif>
    <g:elseif test="${hideSSBHeaderComps}">
        <g:set var="url" value="${url+'?hideSSBHeaderComps='+hideSSBHeaderComps}" />
    </g:elseif>
    <script type="text/javascript">
        // Track calling page for breadcrumbs
        (function () {
            // URLs to exclude from updating genMainCallingPage.  No breadcrumbs for "/BannerGeneralSsb/" URLs
            // (i.e. the applicationName variable below) or App Nav should be created for the landing page.
            var referrerUrl = document.referrer,
                excludedRegex = [
                    /\/${applicationName}\//,
                    /\/seamless/
                ],
                isExcluded = false,
                i = 0,
                regexLen = excludedRegex.length;

            if (referrerUrl) {
                for (; i < regexLen; i++) {
                    if (excludedRegex[i].test(referrerUrl)) {
                        isExcluded = true;
                        break;
                    }
                }

                if (!isExcluded) {
                    // Track this page
                    sessionStorage.setItem('genMainCallingPage', referrerUrl);
                }
            }
        })();
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta HTTP-EQUIV="REFRESH" content="0; url=${url}">

    <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
        <asset:stylesheet src="modules/gss-applicationRTL-mf.css"/>
    </g:if>
    <g:else>
        <asset:stylesheet src="modules/gss-applicationLTR-mf.css"/>
    </g:else>

    <asset:javascript src="modules/gss-application-mf.js"/>
</head>
<body>
</body>
</html>
