import arc.Events;
import arc.util.CommandHandler;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType.GameOverEvent;
import mindustry.gen.Player;
import mindustry.maps.Map;
import mindustry.mod.Plugin;
public class ACMD extends Plugin {
    @Override public void registerClientCommands(CommandHandler h){
        h.<Player>register("p", "Toggle pause", (a, p) -> { if(p.admin && !Vars.state.isMenu()) Vars.state.set(Vars.state.isPaused() ? GameState.State.playing : GameState.State.paused); });
        h.<Player>register("m", "[name...]", "Смена карты, /m — список", (a, p) -> {
            if(!p.admin || Vars.state.isMenu()) return;
            var all = Vars.maps.customMaps();
            if(a.length == 0 || a[0].equalsIgnoreCase("l")){ StringBuilder sb = new StringBuilder("Карты:\n"); for(int i = 0; i < all.size; i++) sb.append(i + 1).append(". ").append(all.get(i).plainName()).append('\n'); p.sendMessage(sb.toString()); return; }
            Map map = null; if(Strings.canParseInt(a[0])){ int n = Strings.parseInt(a[0], 0) - 1; if(n >= 0 && n < all.size) map = all.get(n); }
            if(map == null){ String q = a[0].toLowerCase(); map = all.find(m -> m.plainName().toLowerCase().contains(q)); }
            if(map == null){ p.sendMessage("[scarlet]Карта не найдена. /m — список."); return; }
            p.sendMessage("[accent]Смена на: [white]" + map.plainName());
            Vars.maps.setNextMapOverride(map);
            Events.fire(new GameOverEvent(Vars.state.rules.waveTeam));
        });
    }
}
