import { useEffect } from "react";
import { RABBIT_MAP } from "../../constants/common/RabbitMap";
import { RABBIT_STATE_MAP } from "../../constants/game/RabbitStateMap";
import { ShopType } from "../../types/ShopType";

interface ShopCharacterPreviewProps {
  outfit: [cap: number, face: number, clothing: number];
  color: number;
  shopAllItem: ShopType;
}

export const ShopCharacterPreview = ({ color, outfit, shopAllItem }: ShopCharacterPreviewProps) => {
  useEffect(() => {}, [shopAllItem]);
  return (
    <>
      <img
        src={RABBIT_MAP[color].IMG[RABBIT_STATE_MAP.STAND]}
        className="absolute 3xl:w-[600px] w-[480px] 3xl:h-[600px] h-[480px]"
      />
      <img
        src={`data:image/png;base64,${shopAllItem.clothing.length > 0 && shopAllItem.clothing[outfit[2]].image}`}
        className="absolute 3xl:w-[600px] w-[480px] 3xl:h-[600px] h-[480px]"
      />
      <img
        src={`data:image/png;base64,${shopAllItem.face.length > 0 && shopAllItem.face[outfit[1]].image}`}
        className="absolute 3xl:w-[600px] w-[480px] 3xl:h-[600px] h-[480px]"
      />
      <img
        src={`data:image/png;base64,${shopAllItem.cap.length > 0 && shopAllItem.cap[outfit[0]].image}`}
        className="absolute 3xl:w-[600px] w-[480px] 3xl:h-[600px] h-[480px]"
      />
    </>
  );
};
