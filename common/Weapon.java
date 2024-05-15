import java.io.Serializable;

public abstract class Weapon implements Serializable {

    public boolean reloading;
    public float reloadSpeed;
    public float reloadTimer;
    public int maxAmmo;
    public int ammo;
    public boolean magazined;

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
    }

    public static class Remington870 extends Weapon {

        public Remington870() {
            reloadSpeed = 0.5f;
            maxAmmo = 5;
            ammo = 5;
            magazined = false;
        }

        public void draw(Player player) {}

        public boolean shoot(World world, Player player) {
            if (ammo > 0) {
                ammo--;
                for (int i = 0; i < 8; i++) {
                    world.add(
                        new Bullet(
                            player.pos,
                            5,
                            player.angle + (float) Math.random() / 4,
                            500 + (float) Math.random() * 25
                        )
                    );
                }
            } else {
                reload();
            }
            return false;
        }
    }

    public String toString() {
        return String.format(
            "Remington 870 - %d | %d %s",
            ammo,
            maxAmmo,
            reloading ? String.format("Reloading %.2fs", reloadTimer) : ""
        );
    }
}
