# Dispenser API

Dispenser API is an API to provide Minecraft resources through a REST API.
Is considered as "resources" all files that isn't a compiled Java class, so assets (client-side) and data (server-side).

This API is updated automatically (every hours) and work for any Minecraft version (release, release-candidate, pre-release, snapshot) having the considered resource type.

A hosting version of the API is soon available.

## Current provided resources (v1.0)

| Asset                 | URI             | Output type |
| --------------------- | --------------- | ----------- |
| Block Textures        | /block/textures | PNG         |
| Block Models          | /block/models   | JSON        |
| Block Textures MCMETA | /block/mcmetas  | JSON        |

## Planned resources

| Asset              | Data         |
| ------------------ | ------------ |
| Blockstates        | Tags         |
| Item Textures      | Structures   |
| Item Models        | Recipes      |
| Entity Textures    | Advancements |
| Particles Textures | Loot Tables  |
| Paintings Textures | Commands     |
| Langs              | Block List   |
| Sounds             | Item List    |

## Contribution

Feel free to contribute to the project by helping with development, doing code reviews, suggesting improvements or new features...
