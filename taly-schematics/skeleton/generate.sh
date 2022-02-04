#!/usr/bin/env bash
# run our schematics to generate an application from the configuration files given in config/
# --name (We pass in the technical name of the application)
# --ndbx.expert to enable expert mode which will caus some layout to change. Absence is retail, you can't pass false
# --directory (The location to generate the app, default is `generated` so we could omit this)
# --configDirectory (The location of the config folder, default is `/config` so we could omit this)
# --env to pass in some env files. You can also omit the value assignment to pick the value from the system environment if available
# --debugInspection --showDebugPanel --force are specific to this demo to enable debugging tools and overwrite existing files (force).
yarn schematics @allianz/taly-schematics:generate-app  \
--name="my-itmp-app" \
--ndbx.expert \
--directory=generated \
--env BFF_BASE_URL=https://api-test.allianz.com/itmp-bff-quote-and-buy-integration \
--configDirectory=config/ \
--debugInspection --showDebugPanel --force \
"$@" # $@ forwards any additional given parameter
