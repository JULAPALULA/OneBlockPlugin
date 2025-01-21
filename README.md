![Alt Text](https://i.imgur.com/iaOWChk.png)
## Plugin Description

This Minecraft plugin allows administrators to create lots filled with specific materials. Players can buy these lots, and once purchased, they can enable them. Every minute, players will receive a random item from the lot, testing their luck! The lot’s materials are gathered by the player, who needs to place blocks to obtain the items, adding an element of excitement and randomness.

### Key Features:
- **Lot Creation by Admins:** Administrators can define lots with specific materials.
- **Lot Purchase by Players:** Players can buy lots, gaining access to the materials within.
- **Enable Lots After Purchase:** Once a lot is bought, players can enable it in their pool.
- **Random Item Distribution:** Every minute, players will receive a random item from their enabled lot, offering an element of luck and surprise.
- **Interactive Gameplay:** Players must place blocks to gather score, which allows players to buy lots.

This plugin is perfect for adding a new layer of interactivity and luck to your server, where players can test their fortune while collecting valuable items from lots they own.
## License
This software is licensed under a modified MIT License.

- **Non-Commercial Use:** You are free to use, copy, and modify the software for personal or educational purposes.
- **Commercial Use:** Commercial use is prohibited unless you obtain explicit permission from the author.

---

## Usage

### Available Commands:
- `/lot` - Opens lot menu.
- `/lot help` - Display this help message.
- `/lot show <lot name>` - Show details of a specific lot.
- `/lot list` - List all lots and their status.
- `/lot score` - Display your current stats and score.
- `/lot buy <lot name>` - Buy a specific lot.
- `/lot enable <lot name>` - Enable a lot from your pool.
- `/lot disable <lot name>` - Disable a lot from your pool.

---

### Server Setup:
1. Download Minecraft Paper or Bukkit server version 1.21.1.
2. Place the jar file into the server's `plugins` folder.
3. Launch the server. A folder called `one_block_data` will be created, containing another folder named `lots`.
4. Inside the `lots` folder, place a `<lot name>.lot.json` file, where the `<lot name>` is the name assigned to the lot. The structure must follow this format:

   Example `example.lot.json` file:
    ```json
    {
        "score": 10,
        "materials": ["ANDESITE", "AIR"]
    }
    ```

First time opening the server with the plugin will create a free lot called "all.lot.json", with all items and blocks allowed of Minecraft version 1.21.1.
If you want to create a new lot with specific materials, names can be found here: [Bukkit Material Documentation](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html).

Also for better experience, is recommended to use [Sky Void World Gen](https://modrinth.com/datapack/skyblock-void-worldgen/version/2.0.2) to make the survival experience even harder!

---

### Disclaimer:
- Lot names cannot contain special characters, including spaces.
- If the server cannot find or validate any lots, the plugin will disable itself.

---
