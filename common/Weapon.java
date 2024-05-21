import java.io.Serializable;

public abstract class Weapon implements Serializable {

    public String name;
    public boolean reloading;
    public float reloadSpeed;
    public float reloadTimer;
    public int maxAmmo;
    public int ammo;
    public boolean magazined;
    public float shotCooldown;
    public float shotTimer;
    public int len1;
    public int len2;
    public int wid1;
    public int wid2;
    public Color c1;
    public Color c2;

    public abstract boolean shoot(World world, Player player);

    public abstract void draw(Player player);

    public void reload() {
        if (ammo == maxAmmo) return;
        if (!reloading) reloadTimer = reloadSpeed;
        reloading = true;
    }

    public void update(float dt) {
        if (reloading) {
            reloadTimer -= dt;
            if (reloadTimer <= 0) {
                if (magazined) {
                    ammo = maxAmmo;
                    reloading = false;
                } else {
                    ammo++;
                    if (ammo == maxAmmo) reloading = false;
                    reloadTimer = reloadSpeed;
                }
            }
        }
        shotTimer -= dt;
    }

    public String toString() {
        return String.format(
            "%s - %d | %d %s",
            name,
            ammo,
            maxAmmo,
            reloading ? String.format("Reloading %.2fs", reloadTimer) : ""
        );
    }

//  --------------------------------
//            Remington870
//  --------------------------------

    public static class Remington870 extends Weapon {

        public Remington870() {
            name = "Remington 870";
            reloadSpeed = 0.8f;
            maxAmmo = 5;
            ammo = 5;
            magazined = false;
            shotCooldown = 0.5f;
            len1 = 40;
            len2 = 30;
            wid1 = 16;
            wid2 = 20;
            c1 = new Color(140, 136, 126);
            c2 = new Color(105, 102, 94);
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                for (int i = 0; i < 8; i++) {
                    world.add(
                        new Bullet(
                            new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                            5,
                            player.angle + ((float) Math.random() - 0.5f) / 2,
                            500 + (float) Math.random() * 25,
                            0.6f,
                            12f,
                            new Color(175, 155, 96)
                        )
                    );
                }
                shotTimer = shotCooldown;
            } else if (ammo <= 0) {
                reload();
            }
            return false;
        }
    }

//  ------------------------
//            AK47
//  ------------------------

    public static class Ak47 extends Weapon {

        public Ak47() {
            name = "AK-47";
            reloadSpeed = 3f;
            maxAmmo = 30;
            ammo = 30;
            magazined = true;
            shotCooldown = 0.1f;
            len1 = 40;
            len2 = 20;
            wid1 = 8;
            wid2 = 6;
            c1 = new Color(140, 136, 126);
            c2 = new Color(105,74,1);
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                world.add(
                    new Bullet(
                        new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                        5,
                        player.angle + (float) (Math.random() / 8 - 0.0625),
                        500,
                        2f,
                        8f,
                        new Color(175, 155, 96)
                    )
                );
                shotTimer = shotCooldown;
            } else if (ammo <= 0) {
                reload();
                return false;
            }
            return true;
        }
    }

//  --------------------------
//            Pistol
//  --------------------------

    public static class Pistol extends Weapon {

        public Pistol() {
            name = "Pistol";
            reloadSpeed = 2f;
            maxAmmo = 15;
            ammo = 15;
            magazined = true;
            shotCooldown = 0.3f;
            len1 = 30;
            len2 = 6;
            wid1 = 8;
            wid2 = 6;
            c1 = new Color(140, 136, 126);
            c2 = new Color(105, 102, 94);
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                world.add(
                    new Bullet(
                        new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                        5,
                        player.angle + (float) (Math.random() / 10 - 0.05),
                        500,
                        2f,
                        10f,
                        new Color(137, 97, 78)
                    )
                );
                shotTimer = shotCooldown;
            } else if (ammo <= 0) {
                reload();
                return false;
            }
            return true;
        }
    }

//  --------------------------
//            Sniper
//  --------------------------

    public static class Sniper extends Weapon {

        public Sniper() {
            name = "Sniper";
            reloadSpeed = 4f;
            maxAmmo = 1;
            ammo = 1;
            magazined = true;
            shotCooldown = 1f;
            len1 = 60;
            len2 = 40;
            wid1 = 5;
            wid2 = 7;
            c1 = new Color(105, 102, 94);
            c2 = new Color(79, 76, 70);
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                world.add(
                    new Bullet(
                        new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                        3,
                        player.angle,
                        2500,
                        2f,
                        50f,
                        new Color(36, 36, 36)
                    )
                );
                shotTimer = shotCooldown;
            } else if (ammo <= 0) {
                reload();
                return false;
            }
            return true;
        }
    }

//  -----------------------
//            SMG
//  -----------------------

    public static class SMG extends Weapon {

        public SMG() {
            name = "SMG";
            reloadSpeed = 3f;
            maxAmmo = 40;
            ammo = 40;
            magazined = true;
            shotCooldown = 0f;
            len1 = 45;
            len2 = 40;
            wid1 = 3;
            wid2 = 9;
            c1 = new Color(105, 102, 94);
            c2 = new Color(79, 76, 70);
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                world.add(
                    new Bullet(
                        new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                        3,
                        player.angle + (float) (Math.random() - 0.5),
                        300,
                        1f,
                        5f,
                        new Color(173, 164, 140)
                    )
                );
                shotTimer = shotCooldown;
            } else if (ammo <= 0) {
                reload();
                return false;
            }
            return true;
        }
    }

//  --------------------------------
//            Flamethrower
//  --------------------------------

    public static class Flamethrower extends Weapon {

        public Flamethrower() {
            name = "Flamethrower";
            reloadSpeed = 3f;
            maxAmmo = 150;
            ammo = 150;
            magazined = true;
            shotCooldown = 0f;
            len1 = 40;
            len2 = 15;
            wid1 = 6;
            wid2 = 8;
            c1 = new Color(140, 136, 126);
            c2 = new Color(105,74,1);
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                for(int i = 0; i < 3; i++)
                    world.add(
                        new Bullet(
                            new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                            2,
                            player.angle + (float) (Math.random() * 0.5 - 0.25),
                            283,
                            1f,
                            1f,
                            new Color(235, 113, 26)
                        )
                    );
                shotTimer = shotCooldown;
            } else if (ammo <= 0) {
                reload();
                return false;
            }
            return true;
        }
    }

//  -----------------------
//            RPG
//  -----------------------

public static class RPG extends Weapon {

    public RPG() {
        name = "RPG";
        reloadSpeed = 8f;
        maxAmmo = 1;
        ammo = 1;
        magazined = true;
        shotCooldown = 2f;
        len1 = 80;
        len2 = 40;
        wid1 = 16;
        wid2 = 8;
        c1 = new Color(105, 102, 94);
        c2 = new Color(79, 76, 70);
    }

    public void draw(Player player) {}

    public boolean shoot(World world, Player player) {
        if (ammo > 0 && shotTimer <= 0) {
            reloading = false;
            ammo--;
            for(int i = 0; i < 5; i++)
                world.add(
                    new Bullet(
                        new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                        16,
                        player.angle,
                        500,
                        5f,
                        20f,
                        new Color(53, 69, 48)
                    )
                );
            shotTimer = shotCooldown;
        } else if (ammo <= 0) {
            reload();
            return false;
        }
        return true;
    }
}

//  ----------------------------
//            Debugger
//  ----------------------------

    public static class Debugger extends Weapon {

        public Debugger() {
            name = "uuddlrlrba";
            reloadSpeed = 0.5f;
            maxAmmo = 1000;
            ammo = 1000;
            magazined = true;
            shotCooldown = 0f;
            len1 = 60;
            len2 = 30;
            wid1 = 3;
            wid2 = 6;
            c1 = new Color(191, 157, 0);
            c2 = new Color(255, 210, 0);
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                for(int i = 0; i < 15; i++)
                    world.add(
                        new Bullet(
                            new Vec2(player.pos.x + (float) (Math.cos(player.angle) * len1), player.pos.y + (float) (Math.sin(player.angle) * len1)),
                            5,
                            player.angle + (float) (Math.random() - 0.5),
                            283,
                            3f,
                            50f,
                            new Color(255, 215, 0)
                        )
                    );
                shotTimer = shotCooldown;
            } else if (ammo <= 0) {
                reload();
                return false;
            }
            return true;
        }
    }

    public String getName() {
        return name;
    }
}
