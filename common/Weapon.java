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

    public static class Remington870 extends Weapon {

        public Remington870() {
            name = "Remington 870";
            reloadSpeed = 0.5f;
            maxAmmo = 5;
            ammo = 5;
            magazined = false;
            shotCooldown = 0.5f;
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                for (int i = 0; i < 8; i++) {
                    world.add(
                        new Bullet(
                            player.pos,
                            5,
                            player.angle + (float) Math.random() / 4,
                            500 + (float) Math.random() * 25,
                            0.4f
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

    public static class Ak47 extends Weapon {

        public Ak47() {
            name = "AK-47";
            reloadSpeed = 3f;
            maxAmmo = 30;
            ammo = 30;
            magazined = true;
            shotCooldown = 0.1f;
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0 && shotTimer <= 0) {
                reloading = false;
                ammo--;
                world.add(
                    new Bullet(
                        player.pos,
                        5,
                        player.angle + (float) Math.random() / 8,
                        500,
                        2f
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
}
