/*
 * Copyright 2014-2015 Open Networking Laboratory
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
package org.onosproject.cli.net;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.DeviceId;
import org.onosproject.incubator.net.resource.label.LabelResourceAdminService;

@Command(scope = "onos", name = "label-pool-destroy",
    description = "Destroys label resource pool by a specific device id")
public class LabelPoolDestroyCommand extends AbstractShellCommand {
    @Argument(index = 0, name = "deviceId", description = "Device identity", required = true, multiValued = false)
    String deviceId = null;

    @Override
    protected void execute() {
        LabelResourceAdminService lrs = get(LabelResourceAdminService.class);
        lrs.destroyDevicePool(DeviceId.deviceId(deviceId));
    }

}
