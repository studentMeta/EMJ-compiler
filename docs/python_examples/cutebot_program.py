#DISCLAIMER : généré par IA pour avoir une idée de ce à quoi pourrait ressembler un fichier python pour cutebot


from microbit import *
# Note : Dans MakeCode, l'import est automatique,
# mais sur un vrai robot on utilise souvent une lib comme 'cutebot'
import cutebot

# --- SECTION : FONCTIONS UTILISATEUR ---
# Traduction de : 🔢 [calculer] (🔢 [a], 🔢 [b]) { ↩️ [a] + [b]; }
def calculer(a, b):
    return a + b

# Traduction de : 👥(🔟) [get_status] () { ↩️ (✅, ❌); }
def get_status():
    # Les tuples EMJ deviennent des listes ou tuples Python
    return [True, False]

# --- SECTION : MAIN (🏠) ---
def main():
    # --- 1. Déclarations et Types ---
    vitesse = 50                 # 🔢 [vitesse] = 50;
    message = "Patrouille"       # 🔡 [msg] = "Patrouille";
    etat = (True, 10)            # Mix de types (si ta grammaire le permet)

    # --- 2. Tuples et Indexation ---
    # 👥(🔢) [coords] = (10, 20);
    coords = [10, 20]
    val_x = coords[0]            # [coords]0️⃣

    # --- 3. Actions Predefined (Le Robot) ---
    # ⬆️(50);
    cutebot.motors(vitesse, vitesse)
    # 🚨();
    cutebot.set_color(cutebot.RGBLights.ALL, 0xff0000)
    # 📻();
    # music.play_tone(440, 500)

    # --- 4. Structures de contrôle ---
    # 🤔 ([vitesse] > 0) { ... }
    if vitesse > 0:
        display.show(Image.HAPPY)
    else:
        # 🤨 { 👇; } -> else: pass
        pass

    # 🔁 (5) { ... }
    for i in range(5):
        cutebot.motors(20, -20) # Tourne un peu
        sleep(200)

    # ♾️ (✅) { ... }
    while True:
        # ✋();
        if button_a.is_pressed():
            cutebot.stop()
            break

    # --- 5. Fin de programme (🌀) ---
    cutebot.stop()

# Lancement du programme
if __name__ == "__main__":
    main()