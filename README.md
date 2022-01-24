# Dispenser API

Dispenser API is an API to provide Minecraft resources through a REST API.
Is considered as "resources" all files that isn't a compiled Java class, so assets (client-side) and data (server-side).

This API is updated automatically (every hours) and work for any Minecraft version (release, release-candidate, pre-release, snapshot) having the considered resource type.

A hosting version of the API is available [here](https://dispenser.gunivers.net/). Thanks to Gunivers for supporting and hosting this project!

Current endpoint: `/api/versions` ([hosted version](https://dispenser.gunivers.net/api/versions))  
Swagger Documentation: `/api/docs` ([hosted version](https://dispenser.gunivers.net/api/docs))  
Discord to follow or/and help this project: [Gunivers' Discord](https://discord.gg/8F7cdm9bqs)

You can test the API through the Swagger documentation.

## Current provided resources (v1.0)


| Asset                 | URI             | Output type |
| ----------------------- | ----------------- | ------------- |
| Block Textures        | /block/textures | PNG         |
| Block Models          | /block/models   | JSON        |
| Block Textures MCMETA | /block/mcmetas  | JSON        |

## Planned resources


| Asset              | Data         |
| -------------------- | -------------- |
| Blockstates        | Tags         |
| Item Textures      | Structures   |
| Item Models        | Recipes      |
| Entity Textures    | Advancements |
| Particles Textures | Loot Tables  |
| Paintings Textures | Commands     |
| Langs              | Block List   |
| Sounds             | Item List    |

## Developpers

### Build the project

In the root folder:
```
mvn package
```
The resulting JAR is in target folder.

### Deploy the project (Docker)

In the root folder:
```
docker-compose -p dispenser-api up -d
```

Endpoint: http://localhost/api/versions  
Swagger Documentation: http://localhost/docs

## Projects using Dispenser API

- Block Renderer - [demo](https://dispenser.gunivers.net/block-renderer) - [git](https://github.com/theogiraudet/Minecraft-Block-Renderer)

## Contribution

Feel free to contribute to the project by helping with development, doing code reviews, suggesting improvements or new features...
