## ${{values.app_name}}

Based on 
[![>= v6.0.0](https://img.shields.io/badge/%40allianz%2Ftaly--schematics-v6.0.0-important)](https://github.developer.allianz.io/ilt/taly-workspace/releases)

A basic setup of the taly schematics to generate a flow-based application based on PFE & Taly and your share of Building Blocks. The configuration files are either delivered by the ITMP UI Editor or manually authored. 

+ config/pages.json: This is the main driver of the generation. Holds the Page & Building Block structure & data. The json file links to a schema definition which supports you in authoring & validation of the file.
+ config/pfe.json: A matching pfe configuration. This file is not evaluated but copied in and used by the provided pfe setup.
+ config/policy.txt. The rules for the internal ACL setup which allows you to adjust any exposed resources in terms of visibility (`view`) and editability (`edit`). Exposed resources can be headlines, paragraphs, images and of course any sort of form element. The author of a Building Block must implement ACL in order to support it on the API side.
+ `config/assets/` put in any assets you refer to in `pages.json` (like for the stage). The folder will be made available in the generated application.

The generation involves the execution of Angular Schematics. This example holds a shell script  `generate.sh` to let us invoke it through an npm task easily.


```
# install everything
npm install

# generate & start the app
npm start

# (optionally) start a watcher to generate whenever you change files in  `config/`
npm generate:watch
```

## Schematics Invocation Break Down

```
# run our schematics to generate an application from the configuration files given in config/

# --directory 
# --configDirectory ()
# --env 
# --debugInspection --showDebugPanel --force are

yarn schematics @allianz/taly-schematics:generate-app  \
# We have to pass in the technical name of the application (required)
--name="my-itmp-app" \
# Enable expert mode which will caus some layout to change. Absence is retail, you can't pass false.
--ndbx.expert \
# The location to generate the app, default is `generated` so we could omit this
--directory=generated \
# Pass in required env values. You can also omit the value assignment to pick the value from the system environment if available
--env BFF_BASE_URL=https://api-test.allianz.com/itmp-bff-quote-and-buy-integration \
# The location of the config folder, default is `/config` (from CWD) so we could omit this here.
--configDirectory=config/ \
# specific to this demo to enable debugging tools and overwrite existing files (force)
--debugInspection --showDebugPanel --force \
"$@" # $@ forwards any additional given parameter
```
