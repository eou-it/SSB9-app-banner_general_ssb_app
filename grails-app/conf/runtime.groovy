/*******************************************************************************
 Copyright 2019-2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/



// ******************************************************************************
//                              CORS Configuration
// ******************************************************************************
// Note: If changing custom header names, remember to reflect them here.
//
cors.url.pattern        = '/api/*'
cors.allow.origin.regex ='.*'
cors.expose.headers     ='content-type,X-hedtech-totalCount,X-hedtech-pageOffset,X-hedtech-pageMaxSize,X-hedtech-message,X-hedtech-Media-Type'


// ******************************************************************************
//                             API Prefix Configuration
// ******************************************************************************
// Specify the URL prefixes used for the API. The list should include all prefixes
// that are used to expose API endpoints, regardless of whether they are protected
// using Basic Authentication or whether they are 'stateless'. See DB Connection
// Caching Configuration below, which may configure a subset of the URL prefixes
// configured here to be 'stateless'.
//
apiUrlPrefixes = [ 'api', 'qapi', 'rest', 'ui' ]


// ******************************************************************************
//                    DB Connection Caching Configuration
// ******************************************************************************
// Note: The BannerDS will cache database connections for administrative users,
//       however for RESTful APIs we do not want this behavior (even when
//       authenticated as an 'administrative' user. RESTful APIs should be stateless.
//
// IMPORTANT:
// When exposing RESTful endpoints for use by external programmatic clients, we
// will want to exclude database caching for those URLs.
// Also, if using a prefix other than 'api' and 'qapi' you will need to ensure
// the spring security filter chain is configured to avoid creating a session.
// Endpoints that will be used via Ajax on behalf of an authenticated user can
// benefit from having 'state' (with respect to their authentication), hence this
// configuration may be a subset of the 'apiUrlPrefixes' configuration above.
//
avoidSessionsFor = [ 'api', 'qapi' ]


// ******************************************************************************
//             RESTful API Custom Response Header Name Configuration
// ******************************************************************************
// Note: Tests within this test app expect this 'X-hedtech...' naming to be used.
//
restfulApi.header.totalCount  = 'X-hedtech-totalCount'
restfulApi.header.pageOffset  = 'X-hedtech-pageOffset'
restfulApi.header.pageMaxSize = 'X-hedtech-pageMaxSize'
restfulApi.header.message     = 'X-hedtech-message'
restfulApi.header.mediaType   = 'X-hedtech-Media-Type'

// ******************************************************************************
//             RESTful API 'Paging' Query Parameter Name Configuration
// ******************************************************************************
// Note: Tests within this test app expect this 'X-hedtech...' naming to be used.
//
restfulApi.page.max    = 'max'
restfulApi.page.offset = 'offset'

// API path component to construct the REST API URL
sspb.apiPath = 'internalPb'

// ******************************************************************************
//                       RESTful API Endpoint Configuration
// ******************************************************************************

restfulApiConfig = {
    marshallerGroups {
        group 'json_date' marshallers {
            marshaller {
                instance = new org.grails.web.converters.marshaller.ClosureObjectMarshaller<grails.converters.JSON>(
                        java.util.Date, {return it?.format( "yyyy-MM-dd" )} )
            }
        }

        group 'xml_date' marshallers {
            marshaller {
                instance = new org.grails.web.converters.marshaller.ClosureObjectMarshaller<grails.converters.XML>(
                        java.util.Date, {return it?.format( "yyyy-MM-dd" )} )
            }
        }
    }

    resource 'jobsub-pending-print' config {
        serviceName = 'jobsubOutputCompositeService'
        methods = ['list', 'show', 'update']
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshallerGroup 'json_date'             //for date related fields
                jsonBeanMarshaller {
                    supports net.hedtech.banner.general.jobsub.JobsubExternalPrinter
                    includesFields {
                        field 'id'
                        field 'version'
                        field 'job'
                        field 'oneUpNo'
                        field 'fileName'
                        field 'printer'
                        field 'printForm'
                        field 'printDate'
                        field 'creatorId'
                        field 'printerCommand'
                        field 'mime'
                    }
                }
                jsonBeanMarshaller {
                    supports net.hedtech.banner.general.jobsub.JobsubSavedOutput
                    includesFields {
                        field 'id'
                        field 'version'
                        field 'job'
                        field 'fileName'
                        field 'printer'
                        field 'printForm'
                        field 'printDate'
                        field 'jobsubOutput'
                    }
                }
            }
            jsonExtractor {
                property 'job' name 'job'
                property 'id' name 'id'
                property 'printer' name 'printer'
                property 'jobsubOutput' name 'jobsubOutput'
            }
        }
        representation {
            mediaTypes = ["application/octet-stream"]
            marshallerFramework = 'jobsubOutputMarshaller'
        }
    }

    // Pagebuilder resources

    // generic resource for virtual domains

    anyResource {
        serviceName = 'virtualDomainResourceService'
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaTypes = ["application/xml"]
            //jsonAsXml = true
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.xml.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 200
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.MapExtractor()
        }
    }

    resource 'pagesecurity' config {
        serviceName= 'pageSecurityService'
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
    }


    resource  'pages' config {
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaTypes = ["application/xml"]
            //jsonAsXml = true
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.xml.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 200
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.MapExtractor()
        }
    }

    resource 'csses' config {
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaTypes = ["application/xml"]
            //jsonAsXml = true
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.xml.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 200
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.MapExtractor()
        }
    }

    resource 'pageexports' config {
        serviceName= 'pageExportService'
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
    }
    resource 'virtualdomainexports' config {
        serviceName= 'virtualDomainExportService'
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
    }
    resource 'cssexports' config {
        serviceName= 'cssExportService'
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
    }

    // This pseudo resource is used when issuing a query using a POST. Such a POST is made
    // against the actual resource being queried, but using a different URL prefix (e.g., qapi)
    // so the request is routed to the 'list' method (versus the normal 'create' method).
    resource 'query-filters' config {
        // TODO: Add support for 'application/x-www-form-urlencoded'
        representation {
            mediaTypes = ["application/json"]
            jsonExtractor {}
        }
    }

    // 2 demo resources
    resource 'todos' config {
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaTypes = ["application/xml"]
            //jsonAsXml = true
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.xml.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 200
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.MapExtractor()
        }
    }

    resource 'projects'  config {
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaTypes = ["application/xml"]
            //jsonAsXml = true
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.xml.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 200
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.MapExtractor()
        }
    }

}