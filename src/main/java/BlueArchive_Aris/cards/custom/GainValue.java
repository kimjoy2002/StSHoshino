package BlueArchive_Aris.cards.custom;

import BlueArchive_Aris.cards.CustomGameCard;

import java.util.List;

public interface GainValue {
    int value(CustomGameCard.ability baseAbility, List<CustomGameCard.ability> abilityList);
    public class GainInt implements GainValue {
        int int_;
        public GainInt(int int_){
            this.int_ = int_;
        };
        @Override
        public int value(CustomGameCard.ability baseAbility, List<CustomGameCard.ability> abilityList) {
            return int_;
        }
    }
}
