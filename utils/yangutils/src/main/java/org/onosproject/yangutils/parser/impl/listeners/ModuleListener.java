/*
 * Copyright 2016 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.yangutils.parser.impl.listeners;

import org.onosproject.yangutils.datamodel.YangModule;
import org.onosproject.yangutils.parser.antlrgencode.GeneratedYangParser;
import org.onosproject.yangutils.parser.exceptions.ParserException;
import org.onosproject.yangutils.parser.impl.TreeWalkListener;

import static org.onosproject.yangutils.parser.impl.parserutils.ListenerUtil.getValidIdentifier;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorLocation.ENTRY;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorLocation.EXIT;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorMessageConstruction.constructListenerErrorMessage;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorType.INVALID_HOLDER;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorType.MISSING_CURRENT_HOLDER;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorType.MISSING_HOLDER;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerValidation.checkStackIsEmpty;
import static org.onosproject.yangutils.parser.impl.parserutils.ListenerValidation.checkStackIsNotEmpty;
import static org.onosproject.yangutils.utils.YangConstructType.MODULE_DATA;

/*
 * Reference: RFC6020 and YANG ANTLR Grammar
 *
 * ABNF grammar as per RFC6020
 * module-stmt         = optsep module-keyword sep identifier-arg-str
 *                       optsep
 *                       "{" stmtsep
 *                           module-header-stmts
 *                           linkage-stmts
 *                           meta-stmts
 *                           revision-stmts
 *                           body-stmts
 *                       "}" optsep
 *
 * ANTLR grammar rule
 * module_stmt : MODULE_KEYWORD identifier LEFT_CURLY_BRACE module_body* RIGHT_CURLY_BRACE;
 */

/**
 * Implements listener based call back function corresponding to the "module"
 * rule defined in ANTLR grammar file for corresponding ABNF rule in RFC 6020.
 */
public final class ModuleListener {

    /**
     * Creates a new module listener.
     */
    private ModuleListener() {
    }

    /**
     * It is called when parser receives an input matching the grammar rule
     * (module), perform validations and update the data model tree.
     *
     * @param listener Listener's object
     * @param ctx context object of the grammar rule
     */
    public static void processModuleEntry(TreeWalkListener listener, GeneratedYangParser.ModuleStatementContext ctx) {

        // Check if stack is empty.
        checkStackIsEmpty(listener, INVALID_HOLDER, MODULE_DATA, ctx.identifier().getText(), ENTRY);

        String identifier = getValidIdentifier(ctx.identifier().getText(), MODULE_DATA, ctx);

        YangModule yangModule = new YangModule();
        yangModule.setName(identifier);

        if (ctx.moduleBody(0).moduleHeaderStatement().yangVersionStatement() == null) {
            yangModule.setVersion((byte) 1);
        }

        listener.getParsedDataStack().push(yangModule);
    }

    /**
     * It is called when parser exits from grammar rule (module), it perform
     * validations and update the data model tree.
     *
     * @param listener Listener's object
     * @param ctx context object of the grammar rule
     */
    public static void processModuleExit(TreeWalkListener listener, GeneratedYangParser.ModuleStatementContext ctx) {

        // Check for stack to be non empty.
        checkStackIsNotEmpty(listener, MISSING_HOLDER, MODULE_DATA, ctx.identifier().getText(), EXIT);

        if (!(listener.getParsedDataStack().peek() instanceof YangModule)) {
            throw new ParserException(constructListenerErrorMessage(MISSING_CURRENT_HOLDER, MODULE_DATA,
                                                                    ctx.identifier().getText(), EXIT));
        }
    }
}
