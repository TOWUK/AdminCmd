import arc.util.CommandHandler;
import arc.util.Reflect;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.gen.Player;
import mindustry.maps.Map;
import mindustry.mod.Plugin;
public class AdminCmd extends Plugin {
    private CommandHandler sh;
    @Override
    public void init() { sh = arc.Core.app.getListeners().find(l -> l.getClass().getSimpleName().equals("ServerControl")) instanceof Object c ? Reflect.get(c, "handler") : null;}
    @Override
    public void registerClientCommands(CommandHandler h) {
        h.register("p", "Toggle pause", (String[] a, Player p) -> { if(p.admin) Vars.state.set(Vars.state.is(GameState.State.paused) ? GameState.State.playing : GameState.State.paused); });
        h.register("m", "<name...>", "Change map or /m l", (String[] a, Player p) -> {
            if (!p.admin) return;
            if (a[0].equalsIgnoreCase("l")) {
                var maps = Vars.maps.customMaps();
                if (!maps.isEmpty()) p.sendMessage("Maps:\n" + maps.map(Map::plainName).toString("\n")); return;
            }
            String search = a[0].replace("_", " ").toLowerCase();
            Map map = Vars.maps.all().find(m -> m.plainName().toLowerCase().contains(search));
            if (map != null) sh.handleMessage("nextmap " + map.plainName().replace(" ", "_")); sh.handleMessage("gameover");
        });
    }
}
