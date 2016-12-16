/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv.internal;

import org.seedstack.shed.exception.ErrorCode;

enum SuperCsvErrorCode implements ErrorCode {
    ERROR_DURING_SUPER_CSV_PARSING,
    ERROR_DURING_SUPER_CSV_RENDERING,
    ERROR_LOADING_SUPER_CSV_TEMPLATE
}
