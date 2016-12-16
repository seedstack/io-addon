/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.jasper.internal;

import org.seedstack.shed.exception.ErrorCode;

enum JasperErrorCode implements ErrorCode {
    ERROR_DURING_JASPER_REPORT_COMPILATION,
    ERROR_DURING_JASPER_REPORT_RENDERING,
    ERROR_LOADING_JASPER_REPORT
}
