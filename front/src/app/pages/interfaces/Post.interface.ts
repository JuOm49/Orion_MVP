import { User } from "@core/interfaces/user.interface";

import { Subject as SubjectForPost } from "@pages/interfaces/Subject.interface";
import { Comment as CommentPost } from "@pages/interfaces/Comment.interface";

export interface Post {
    id: number;
    title: string;
    description: string;
    createdAt: Date;
    updatedAt: Date;
    user: User;
    subject: SubjectForPost;
    comments?: CommentPost[];
}