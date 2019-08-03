export interface ITruckStop {
  id?: number;
  name?: string;
  basePrice?: number;
  opisPrice?: number;
  street?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  racingCode?: string;
  ownerLogin?: string;
  ownerId?: number;
}

export const defaultValue: Readonly<ITruckStop> = {};
