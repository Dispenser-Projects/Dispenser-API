package fr.theogiraudet.dispenser_api.dao.mongo;

import fr.theogiraudet.dispenser_api.domain.VersionInformation;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Interface to define Minecraft Version Repository for MongoDB
 */
interface MinecraftVersionRepoMongo extends MongoRepository<VersionInformation, String> {}
