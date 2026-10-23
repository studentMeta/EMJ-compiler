#DISCLAIMER : généré par IA pour avoir une idée de ce à quoi pourrait ressembler un fichier python pour cutebot

# environment.py - Gestion de la logique de la carte EMJ
class EMJMap:
    def __init__(self, rows, cols, initial_dir):
        self.rows = rows
        self.cols = cols
        self.direction = initial_dir # 'UP', 'DOWN', 'LEFT', 'RIGHT'
        self.grid = []
        self.police_pos = (0, 0)

    def setup_grid(self, elements):
        """Prend la liste des symboles (🚔, 🛣️, 🦹, etc.) et crée la matrice."""
        for i in range(0, len(elements), self.cols):
            row = elements[i : i + self.cols]
            self.grid.append(row)
            if "🚔" in row:
                self.police_pos = (len(self.grid) - 1, row.index("🚔"))

    def get_cell(self, r, c):
        if 0 <= r < self.rows and 0 <= c < self.cols:
            return self.grid[r][c]
        return "🌋" # Obstacle par défaut si hors limite

# Exemple d'initialisation correspondant à 🗺️ with 3,3,⬆️;
map_emj = EMJMap(3, 3, "UP")
# Correspond aux éléments 🚔 🛣️ 🛣️ 🛣️ 🦹 🛣️ 🛣️ 🛣️ 🏘️
map_emj.setup_grid(["🚔", "🛣️", "🛣️", "🛣️", "🦹", "🛣️", "🛣️", "🛣️", "🏘️"])