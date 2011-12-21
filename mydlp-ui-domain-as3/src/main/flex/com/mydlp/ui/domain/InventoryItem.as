/**
 * Generated by Gas3 v2.0.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package com.mydlp.ui.domain {

    [Managed]
    [RemoteClass(alias="com.mydlp.ui.domain.InventoryItem")]
    public class InventoryItem extends InventoryItemBase {
		
		[Embed("../../../../../../../../mydlp-ui-flex/src/main/flex/assets/icons/16x16/wired.png")]
		public static const ICON_NETWORK:Class;
		
		[Embed("../../../../../../../../mydlp-ui-flex/src/main/flex/assets/icons/16x16/page.png")]
		public static const ICON_INFORMATION_TYPE:Class;
		
		private var _icon:Object = null;
		
		public function get icon(): Object
		{
			if (_icon == null)
			{
				if (this is InventoryItem)
				{
					var iitem:Item = (this as InventoryItem).item;
					if (iitem is Network)
						_icon = ICON_NETWORK;
					else if (iitem is InformationType)
						_icon = ICON_INFORMATION_TYPE;
				}
			}
			return _icon;
		}
		
    }
}