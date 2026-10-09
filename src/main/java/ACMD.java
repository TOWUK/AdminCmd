import arc.Events;
import arc.util.CommandHandler;
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
            if(a.length == 0){ StringBuilder sb = new StringBuilder("Карты:\n"); for(int i = 0; i < all.size; i++) sb.append(i + 1).append(". ").append(all.get(i).plainName()).append('\n'); p.sendMessage(sb.toString()); return; }
            String q = a[0].toLowerCase();
            Map map = all.find(m -> m.plainName().toLowerCase().contains(q));
            if(map == null){ p.sendMessage("[scarlet]Карта не найдена."); return; }
            Vars.maps.setNextMapOverride(map);
            Events.fire(new GameOverEvent(Vars.state.rules.waveTeam));
        });
    }
}
