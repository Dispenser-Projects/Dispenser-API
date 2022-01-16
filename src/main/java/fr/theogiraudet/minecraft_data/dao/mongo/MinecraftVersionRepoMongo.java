package fr.theogiraudet.minecraft_data.dao.mongo;

import fr.theogiraudet.minecraft_data.domain.VersionInformation;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Interface to define Minecraft Version Repository for MongoDB
 */
interface MinecraftVersionRepoMongo extends MongoRepository<VersionInformation, String> {}
