import { User } from "@core/interfaces/user.interface";

import { Post } from "@pages/interfaces/Post.interface";

export interface Comment {
    id: number;
    message: string;
    createdAt: Date;
    updatedAt: Date;
    user: User;
    post: Post;
}