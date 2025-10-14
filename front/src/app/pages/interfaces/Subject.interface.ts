import { Subscription as SubscriptionForUser } from "@pages/interfaces/Subscription.interface";

export interface Subject {
    id: number;
    title: string;
    description: string;
    subscriptionByUser?: SubscriptionForUser | null;
}