package com.videoannotator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
public class UrlLocaleResolver implements LocaleResolver {

    private static final String URL_LOCALE_ATTRIBUTE_NAME = "URL_LOCALE_ATTRIBUTE_NAME";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.info(uri);
        String prefixEn = request.getServletContext().getContextPath() + "/en/";
        String prefixVi = request.getServletContext().getContextPath() + "/vi/";

        Locale locale = null;

        // English
        if (uri.startsWith(prefixEn)) {
            locale = Locale.ENGLISH;
        }
        // Vietnamese
        else if (uri.startsWith(prefixVi)) {
            locale = new Locale("vi", "VN");
        }
        if (locale != null) {
            request.getSession().setAttribute(URL_LOCALE_ATTRIBUTE_NAME, locale);
        }
        if (locale == null) {
            locale = (Locale) request.getSession().getAttribute(URL_LOCALE_ATTRIBUTE_NAME);
            if (locale == null) {
                locale = Locale.ENGLISH;
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // Nothing
    }

}
