package net.hedtech.banner.general.common

import grails.converters.JSON
import org.springframework.context.i18n.LocaleContextHolder
    class GeneralSSBTagLib {
        def i18n_setup = { attrs ->
            def map = [:]
            grailsApplication.mainContext.getBean('messageSource').getMergedBinaryPluginProperties(LocaleContextHolder.getLocale()).properties.each { key ->
                map.put key.key, key.value
            }
            grailsApplication.mainContext.getBean('messageSource').getMergedProperties(LocaleContextHolder.getLocale()).properties.each { key ->
                map.put key.key, key.value
            }
            out << "window.i18n = ${map as JSON};\n"
        }

    }
