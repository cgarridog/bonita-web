/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.user.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.common.component.button.MoreButton;
import org.bonitasoft.console.client.user.task.action.TaskClaimAction;
import org.bonitasoft.console.client.user.task.action.TaskRelaseAction;
import org.bonitasoft.console.client.user.task.action.UserTasksHideAction;
import org.bonitasoft.console.client.user.task.action.UserTasksUnhideAction;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonPrimaryAction;

/**
 * @author Vincent Elcrin
 * 
 */
public class TaskButtonFactory {

    public Button createPerformTaskButton(Action performTask) {
        return new ButtonPrimaryAction("btn-perform", _("Do it"), _("Perform"), performTask);
    }

    public Button createMoreDetailsButton(final Action more) {
        return new MoreButton(_("Show more details about this task"), more);
    }

    public ButtonAction createRetrieveButton(UserTasksUnhideAction userTasksUnhideAction) {
        return new ButtonAction("btn-retrieve", _("Retrieve"),
                _("Stop ignoring this task so I will see it in available tasks"),
                userTasksUnhideAction);
    }

    public ButtonAction createIgnoreButton(UserTasksHideAction userTasksHideAction) {
        return new ButtonAction("btn-ignore", _("Hide"),
                _("Ignore this task so I will no longer see it. Other allowed users will still see it"),
                userTasksHideAction);
    }

    public ButtonAction createUnassignedButton(TaskRelaseAction taskRelaseAction) {
        return new ButtonAction("btn-unassign", _("Release"), _("Unassign this task. Other allowed users will see it"),
                taskRelaseAction);
    }

    public ButtonAction createClaimButton(TaskClaimAction taskClaimAction) {
        return new ButtonAction("btn-assigntome", _("Take"),
                _("Assign this task to me. Other allowed users will no longer see it"), taskClaimAction);
    }
}
