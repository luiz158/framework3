/*
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 * ----------------------------------------------------------------------------
 * This file is part of Demoiselle Framework.
 *
 * Demoiselle Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 * ----------------------------------------------------------------------------
 * Este arquivo é parte do Framework Demoiselle.
 *
 * O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 * modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 * do Software Livre (FSF).
 *
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 * GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 * APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 * para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 * "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 * ou escreva para a Fundação do Software Livre (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */
package org.demoiselle.rest.internal.implementation;

import org.demoiselle.annotation.literal.NameQualifier;
import org.demoiselle.rest.HttpViolationException;
import org.demoiselle.rest.HttpViolationException.Violation;
import org.demoiselle.util.ResourceBundle;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

/**
 * @author SERPRO
 */
@Provider
public class HttpViolationExceptionMapper implements ExceptionMapper<HttpViolationException> {

    private transient ResourceBundle bundle;

    private transient Logger logger;

    @Override
    public Response toResponse(HttpViolationException exception) {
        Set<Violation> violations = exception.getViolations();
        int status = exception.getStatusCode();
        String mediaType = exception.getMediaType();

        if (violations.isEmpty()) {
            violations = null;
        } else {
            getLogger().log(FINE, getBundle().getString("mapping-violations", status), exception);
        }

        return Response.status(status).entity(violations).type(mediaType).build();
    }

    private ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = CDI.current().select(ResourceBundle.class, new NameQualifier("demoiselle-rest-bundle")).get();
        }

        return bundle;
    }

    private Logger getLogger() {
        if (logger == null) {
            logger = CDI.current().select(Logger.class, new NameQualifier("org.demoiselle.exception")).get();
        }

        return logger;
    }
}
